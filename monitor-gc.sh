#!/bin/bash
# Script para monitorear Garbage Collector
# Ejecutar: ./monitor-gc.sh

echo "🔍 Monitoreando Garbage Collector..."
echo "=================================="

# Función para obtener métricas GC
get_gc_metrics() {
    echo "📊 $(date): Métricas de Garbage Collector"

    # Número de ejecuciones y tiempo
    curl -s "https://demo-ia-production.up.railway.app/actuator/metrics/jvm.gc.pause" \
        | python -m json.tool \
        | grep -A 1 "COUNT\|TOTAL_TIME\|MAX"

    echo "---"
}

# Monitorear cada 30 segundos
while true; do
    get_gc_metrics
    sleep 30
done
