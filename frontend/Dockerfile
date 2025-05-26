# Estágio de construção com cache otimizado
FROM node:24-alpine as builder

WORKDIR /app

# Cache de dependências
COPY package.json package-lock.json ./
RUN npm ci --silent --legacy-peer-deps --prefer-offline

# Copia o código fonte
COPY . .

# Build de produção
RUN npm run build

# Estágio de produção minimalista
FROM nginx:alpine

# Configuração de segurança
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia artefatos de build
COPY --from=builder /app/dist /usr/share/nginx/html

# Permissões seguras
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]