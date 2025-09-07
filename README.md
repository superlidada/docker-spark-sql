# docker-spark-sql
docker rm -f spark-thrift-hive 2>/dev/null
docker run -d --name spark-thrift-hive \
  -p 10003:10003 \
  -p 4040:4040 \
  --user root \
  -e HIVE_AUTH_USERS="admin:Admin@123,analyst:Analyst@123" \
  -v /本地地址/spark-auth-1.0-SNAPSHOT.jar:/opt/bitnami/spark/jars/custom-auth.jar \
  bitnami/spark:3.5.1 \
  /opt/bitnami/spark/sbin/start-thriftserver.sh \
  --hiveconf hive.server2.thrift.bind.host=0.0.0.0 \
  --hiveconf hive.server2.thrift.port=10003 \
  --hiveconf hive.server2.authentication=CUSTOM \
  --hiveconf hive.server2.custom.authentication.class=com.example.auth.EnvPasswdAuthenticator \
  --conf spark.sql.catalogImplementation=hive \
  --conf spark.sql.warehouse.dir=file:///opt/warehouse \
  --conf spark.hadoop.datanucleus.autoCreateSchema=true \
  --conf 'spark.hadoop.javax.jdo.option.ConnectionURL=jdbc:derby:;databaseName=/opt/metastore_db;create=true' \
  --conf spark.driver.memory=4g \
  --conf spark.executor.memory=4g \
  --conf spark.executor.cores=2 \
  --conf spark.sql.shuffle.partitions=200 \
  --conf spark.memory.fraction=0.6 \
  --conf spark.storage.memoryFraction=0.3
  
