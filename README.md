# Java Spring Boot Serverless application

Using (AWS Proxy wrappers](https://github.com/awslabs/aws-serverless-java-container/wiki/Quick-start---Spring-Boot) and  [`aws-serverless-java-container`](https://github.com/awslabs/aws-serverless-java-container) this application shows how you can easily build Java application and deploy it into AWS Lambda.

Also this application able to handle following AWS Events
 
  * CloudWatch scheduled event
  * SNS Topic message
  * SQS queue message
  
  
This project uses Java8, AWS CLI, AWS SAM CLI and Gradle to build, test and deploy code.
Aslo consult [CircleCI](.circleci/config.yml) for instructions how to setup automated CI/CD build and deployment for this.

## Running locally

To run function locally use `.\sam-local`

Try API endpoint in terminal or browser

```
curl -s http://127.0.0.1:3000/ping | json_pp
```

## Running in AWS

Deploy to cloud using
```bash
./deploy-to-aws
```

Open URL provided in output, for example: https://deadbeef.execute-api.eu-west-1.amazonaws.com/dev/ping

```bash
curl -s https://deadbeef.execute-api.eu-west-1.amazonaws.com/dev/ping | json_pp
{
   "pong" : "Hello, World!"
}
```

Bench time to run, notice that the first run with the same or more concurrent number of requests takes 6 seconds (i.e. Spring boot launch time)
```bash
ab -n 100 -c 20 https://deadbeef.execute-api.eu-west-1.amazonaws.com/dev/ping
```

## Links

  * https://github.com/awslabs/aws-serverless-java-container/
  * https://docs.aws.amazon.com/lambda/latest/dg/configuration-layers.html
  * https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events/src/main/java/com/amazonaws/services/lambda/runtime/events

## Template (FIXME: remove template README)
The my-service project, created with [`aws-serverless-java-container`](https://github.com/awslabs/aws-serverless-java-container).

The starter project defines a simple `/ping` resource that can accept `GET` requests with its tests.

The project folder also includes a `sam.yaml` file. You can use this [SAM](https://github.com/awslabs/serverless-application-model) file to deploy the project to AWS Lambda and Amazon API Gateway or test in local with [SAM Local](https://github.com/awslabs/aws-sam-local).

Using [Maven](https://maven.apache.org/), you can create an AWS Lambda-compatible zip file simply by running the maven package command from the projct folder.
```bash
$ mvn archetype:generate -DartifactId=my-service -DarchetypeGroupId=com.amazonaws.serverless.archetypes -DarchetypeArtifactId=aws-serverless-springboot2-archetype -DarchetypeVersion=1.3 -DgroupId=my.service -Dversion=1.0-SNAPSHOT -Dinteractive=false
$ cd my-service
$ mvn clean package

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.546 s
[INFO] Finished at: 2018-02-15T08:39:33-08:00
[INFO] Final Memory: XXM/XXXM
[INFO] ------------------------------------------------------------------------
```

You can use [AWS SAM Local](https://github.com/awslabs/aws-sam-local) to start your project.

First, install SAM local:

```bash
$ npm install -g aws-sam-local
```

Next, from the project root folder - where the `sam.yaml` file is located - start the API with the SAM Local CLI.

```bash
$ sam local start-api --template sam.yaml

...
Mounting com.amazonaws.serverless.archetypes.StreamLambdaHandler::handleRequest (java8) at http://127.0.0.1:3000/{proxy+} [OPTIONS GET HEAD POST PUT DELETE PATCH]
...
```

Using a new shell, you can send a test ping request to your API:

```bash
$ curl -s http://127.0.0.1:3000/ping | python -m json.tool

{
    "pong": "Hello, World!"
}
``` 

You can use the [AWS CLI](https://aws.amazon.com/cli/) to quickly deploy your application to AWS Lambda and Amazon API Gateway with your SAM template.

You will need an S3 bucket to store the artifacts for deployment. Once you have created the S3 bucket, run the following command from the project's root folder - where the `sam.yaml` file is located:

