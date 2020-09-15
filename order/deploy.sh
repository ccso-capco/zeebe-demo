./gradlew clean build
docker build -t eu.gcr.io/io-capco-692719302307-trai/bradesco/order .
docker push eu.gcr.io/io-capco-692719302307-trai/bradesco/order
kubectl delete deployment.apps/order service/order
kubectl apply -f deployment.yaml

sleep 10
kubectl port-forward svc/order 8080:8080
