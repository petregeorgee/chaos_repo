cd db
docker build -t my-mysql-db .
docker run --network springboot-mysql-net -p 3306:3306 --name mysql-container -d my-mysql-db
docker exec -it c01 bash
mysql -u root -p
show databases;
use login_system1;
show tables;
