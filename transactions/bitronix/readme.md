XA transaction management using Spring Boot, PostgreSQL, RabbitMQ and Bitronix
======
Don't forget to configure PostgreSQL with max_prepared_transactions to be at least as large as max_connections. See http://www.postgresql.org/docs/9.3/static/runtime-config-resource.html