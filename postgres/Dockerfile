# Используем официальный образ PostgreSQL
FROM postgres:16

# Устанавливаем переменные окружения по умолчанию (можно переопределить при запуске)
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword
ENV POSTGRES_DB=mydatabase
ENV PGDATA=/var/lib/postgresql/data

# Копируем все SQL-скрипты в контейнер (инициализация при запуске)
COPY init.sql /docker-entrypoint-initdb.d/

# Настраиваем права доступа и логи
RUN mkdir -p /var/log/postgresql && chown postgres:postgres /var/log/postgresql
VOLUME [“/var/lib/postgresql/data”]

# Открываем стандартный порт PostgreSQL
EXPOSE 5432
