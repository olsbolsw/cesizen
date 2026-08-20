# Build Angular, then serve the compiled SPA with Nginx.
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build \
    && INDEX_FILE="$(find dist -type f -name index.html | head -n 1)" \
    && test -n "$INDEX_FILE" \
    && OUT_DIR="$(dirname "$INDEX_FILE")" \
    && mkdir -p /app-dist \
    && cp -r "$OUT_DIR"/. /app-dist/

FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app-dist/ /usr/share/nginx/html/
EXPOSE 80
HEALTHCHECK --interval=15s --timeout=5s --retries=5 CMD wget -q --spider http://127.0.0.1/ || exit 1
