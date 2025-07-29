# Etapa 1: Build de la aplicación Angular
FROM node:20-alpine AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar package.json y package-lock.json para instalar dependencias
COPY package*.json ./

# Instalar todas las dependencias (incluidas las de desarrollo)
RUN npm ci

# Copiar el código fuente completo
COPY . .

# Build de la aplicación para producción
RUN npm run build -- --configuration=production

# Etapa 2: Servir la aplicación con Nginx
FROM nginx:alpine

# Remover la configuración por defecto de nginx
RUN rm -rf /usr/share/nginx/html/*

# Copiar la configuración personalizada de nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar los archivos compilados desde la etapa de build
COPY --from=build /app/dist/actriz-frontend /usr/share/nginx/html

# Exponer el puerto 80
EXPOSE 80

# Comando por defecto para iniciar nginx
CMD ["nginx", "-g", "daemon off;"]
