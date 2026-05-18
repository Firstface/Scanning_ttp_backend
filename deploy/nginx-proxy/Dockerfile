FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf
COPY certs/localhost.pem /etc/nginx/certs/localhost.pem
COPY certs/localhost-key.pem /etc/nginx/certs/localhost-key.pem
RUN chown -R nginx:nginx /etc/nginx /var/cache/nginx /var/run

EXPOSE 443

USER nginx

CMD ["nginx", "-g", "daemon off;"]
