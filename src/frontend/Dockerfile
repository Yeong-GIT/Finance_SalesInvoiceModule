# Stage 1: Build the React app
FROM node:18 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json files
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the React application
RUN npm run build

# Stage 2: Serve the app with Nginx
FROM nginx:alpine

# Copy the build output to the Nginx HTML directory
COPY --from=build /app/dist /usr/share/nginx/html

# Copy the Nginx configuration file
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Expose the port Nginx will serve on
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
