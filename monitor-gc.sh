#!/bin/bash
# Script para monitorear Garbage Collector con cálculo de frecuencia por hora
# Ejecutar: ./monitor-gc.sh

echo "🔍 Monitoreando Garbage Collector..."
echo "=================================="

# Función para obtener métricas GC con cálculo de frecuencia
get_gc_metrics() {
    echo "📊 $(date): Métricas de Garbage Collector"

    # Obtener métricas de GC
    gc_data=$(curl -s "https://demo-ia-production.up.railway.app/actuator/metrics/jvm.gc.pause")
    uptime_data=$(curl -s "https://demo-ia-production.up.railway.app/actuator/metrics/process.uptime")

    # Extraer valores usando python
    gc_count=$(echo "$gc_data" | python -c "
import json, sys
data = json.load(sys.stdin)
for m in data['measurements']:
    if m['statistic'] == 'COUNT':
        print(int(m['value']))
        break
")

    gc_total_time=$(echo "$gc_data" | python -c "
import json, sys
data = json.load(sys.stdin)
for m in data['measurements']:
    if m['statistic'] == 'TOTAL_TIME':
        print(round(m['value'], 3))
        break
")

    uptime_seconds=$(echo "$uptime_data" | python -c "
import json, sys
data = json.load(sys.stdin)
print(round(data['measurements'][0]['value'], 2))
")

    # Calcular métricas
    uptime_hours=$(echo "$uptime_seconds" | python -c "print(round(float(input()) / 3600, 2))")
    uptime_minutes=$(echo "$uptime_seconds" | python -c "print(round(float(input()) / 60, 1))")

    if [ "$uptime_hours" != "0" ] && [ "$uptime_hours" != "0.0" ]; then
        gc_per_hour=$(echo "$gc_count $uptime_hours" | python -c "
gc, hours = input().split()
if float(hours) > 0:
    print(round(float(gc) / float(hours), 1))
else:
    print('N/A')
")
    else
        gc_per_hour="N/A (app muy reciente)"
    fi

    # Mostrar resultados formateados
    echo "⏱️  Tiempo ejecutándose: ${uptime_minutes} minutos (${uptime_hours} horas)"
    echo "🗑️  Ejecuciones GC total: ${gc_count}"
    echo "📈 Frecuencia GC: ${gc_per_hour} ejecuciones/hora"
    echo "⏲️  Tiempo total en GC: ${gc_total_time} segundos"

    # Evaluar el estado
    if [ "$gc_per_hour" != "N/A (app muy reciente)" ]; then
        gc_per_hour_num=$(echo "$gc_per_hour" | cut -d' ' -f1)
        if (( $(echo "$gc_per_hour_num < 20" | bc -l) )); then
            echo "✅ Estado: EXCELENTE (< 20/hora)"
        elif (( $(echo "$gc_per_hour_num < 50" | bc -l) )); then
            echo "🟡 Estado: ACEPTABLE (20-50/hora)"
        else
            echo "🔴 Estado: PREOCUPANTE (> 50/hora) - Revisar memoria"
        fi
    fi

    echo "---"
}

# Monitorear cada 30 segundos
while true; do
    get_gc_metrics
    sleep 30
done
