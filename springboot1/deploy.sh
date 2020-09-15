./gradlew clean build
docker build -t eu.gcr.io/io-capco-692719302307-trai/bradesco/springboot1 .
docker push eu.gcr.io/io-capco-692719302307-trai/bradesco/springboot1
kubectl delete deployment.apps/springboot1 service/springboot1
kubectl apply -f deployment.yaml
kubectl port-forward svc/springboot1 8080:8080
