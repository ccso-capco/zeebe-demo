./gradlew clean build
docker build -t eu.gcr.io/io-capco-692719302307-trai/bradesco/payment .
docker push eu.gcr.io/io-capco-692719302307-trai/bradesco/payment
kubectl delete deployment.apps/payment service/payment
kubectl apply -f deployment.yaml
