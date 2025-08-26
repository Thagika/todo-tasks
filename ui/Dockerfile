# Stage 1: build
FROM node:22-alpine AS build
WORKDIR /ui
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Serve static files
FROM nginx:alpine
COPY --from=build /ui/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]