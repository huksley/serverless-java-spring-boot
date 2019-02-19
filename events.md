# Different types of events handled

## API HTTP(s) event:

{
    "resource": "/ping",
    "path": "/ping",
    "httpMethod": "GET",
    "headers": {
        "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "accept-encoding": "gzip, deflate, br",
        "accept-language": "en-US,en-FI;q=0.9,en;q=0.8,ru-RU;q=0.7,ru;q=0.6,fi;q=0.5",
        "dnt": "1",
        "Host": "deadbeef.execute-api.eu-west-1.amazonaws.com",
        "upgrade-insecure-requests": "1",
        "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36",
        "X-Amzn-Trace-Id": "Root=1-5c6bc702-3898654289f358229a7a44a1",
        "X-Forwarded-For": "8.8.8.8",
        "X-Forwarded-Port": "443",
        "X-Forwarded-Proto": "https"
    },
    "multiValueHeaders": {
        "accept": [
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"
        ],
        "accept-encoding": [
            "gzip, deflate, br"
        ],
        "accept-language": [
            "en-US,en-FI;q=0.9,en;q=0.8,ru-RU;q=0.7,ru;q=0.6,fi;q=0.5"
        ],
        "dnt": [
            "1"
        ],
        "Host": [
            "deadbeef.execute-api.eu-west-1.amazonaws.com"
        ],
        "upgrade-insecure-requests": [
            "1"
        ],
        "User-Agent": [
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36"
        ],
        "X-Amzn-Trace-Id": [
            "Root=1-5c6bc702-3898654289f358229a7a44a1"
        ],
        "X-Forwarded-For": [
            "8.8.8.8"
        ],
        "X-Forwarded-Port": [
            "443"
        ],
        "X-Forwarded-Proto": [
            "https"
        ]
    },
    "queryStringParameters": null,
    "multiValueQueryStringParameters": null,
    "pathParameters": null,
    "stageVariables": {
        "Stage": "dev"
    },
    "requestContext": {
        "resourceId": "6asu8s",
        "resourcePath": "/ping",
        "httpMethod": "GET",
        "extendedRequestId": "VVwIYF7nDoEFexw=",
        "requestTime": "19/Feb/2019:09:06:10 +0000",
        "path": "/dev/ping",
        "accountId": "849707200000",
        "protocol": "HTTP/1.1",
        "stage": "dev",
        "domainPrefix": "deadbeef",
        "requestTimeEpoch": 1550567170476,
        "requestId": "99788f80-3425-11e9-b73f-cd1fe08a2b6e",
        "identity": {
            "cognitoIdentityPoolId": null,
            "accountId": null,
            "cognitoIdentityId": null,
            "caller": null,
            "sourceIp": "8.8.8.8",
            "accessKey": null,
            "cognitoAuthenticationType": null,
            "cognitoAuthenticationProvider": null,
            "userArn": null,
            "userAgent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36",
            "user": null
        },
        "domainName": "deadbeef.execute-api.eu-west-1.amazonaws.com",
        "apiId": "deadbeef"
    },
    "body": null,
    "isBase64Encoded": false
}

## CloudWatch scheduled event:

{
    "version": "0",
    "id": "2528c5e5-7feb-9a8d-9329-b445a222897f",
    "detail-type": "Scheduled Event",
    "source": "aws.events",
    "account": "849707200000",
    "time": "2019-02-19T08:59:04Z",
    "region": "eu-west-1",
    "resources": [
        "arn:aws:events:eu-west-1:849707200000:rule/ServerlessSpringApi-MyServiceFunctionCheckWebsiteS-17EIBMQX0K6PN"
    ],
    "detail": {}
}

## SNS Topic message

{
    "Records": [
        {
            "EventSource": "aws:sns",
            "EventVersion": "1.0",
            "EventSubscriptionArn": "arn:aws:sns:eu-west-1:849707200000:test-topic:50c099cd-3d0d-4696-903d-cf808b3ce306",
            "Sns": {
                "Type": "Notification",
                "MessageId": "bcdfbdd1-eb20-58a7-bf97-e934236795c2",
                "TopicArn": "arn:aws:sns:eu-west-1:849707200000:test-topic",
                "Subject": null,
                "Message": "dasdsadsa",
                "Timestamp": "2019-02-19T09:01:59.174Z",
                "SignatureVersion": "1",
                "Signature": "Bk2H8nqd9WHlxnBaLjIymS7WK8TJ9baFiSNUn6dcVqRaGqjI6yq66/Qsh2lc09tBTV4eeDonRQWN1nNJfOeBinRunnsoP2DTIfypORo+UsRXGXEUeQJ7tnQUQe52rkVU5G+hX+hafU0hko6NhtCYgxd2ANtCm4klF3oEDybaRFZN1D0W0WZ3u+5jQNQU01rBca8VZ+hCLcQzJtH51p+VKrLYMpVz5h1jFxEB3XTkUtG9Q04bPypuY4o/Qh4mCx1pgghhpeZp6hOvoCWYQrj8EVyJo/SXynXGtK/RPsugRgwCMHRm/TsU1HHJKEVSe8ATMQX1oA6cXfFRzctm9tKnVQ==",
                "SigningCertUrl": "https://sns.eu-west-1.amazonaws.com/SimpleNotificationService-6aad65c2f9911b05cd53efda11f913f9.pem",
                "UnsubscribeUrl": "https://sns.eu-west-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:eu-west-1:849707200000:test-topic:50c099cd-3d0d-4696-903d-cf808b3ce306",
                "MessageAttributes": {}
            }
        }
    ]
}

{
    "Records": [
        {
            "EventSource": "aws:sns",
            "EventVersion": "1.0",
            "EventSubscriptionArn": "arn:aws:sns:eu-west-1:849707200000:test-topic:50c099cd-3d0d-4696-903d-cf808b3ce306",
            "Sns": {
                "Type": "Notification",
                "MessageId": "15d202b9-ecca-5bf1-86aa-565c2460e18e",
                "TopicArn": "arn:aws:sns:eu-west-1:849707200000:test-topic",
                "Subject": null,
                "Message": "hello world!",
                "Timestamp": "2019-02-19T09:03:38.472Z",
                "SignatureVersion": "1",
                "Signature": "Gtt+vpFy8xd5jEBqChL3GU2DFS2WKFzMzmuUCcndy2y+UH4XJZ9TaU082rXtKZdNnhPuGrRygoOJkemLhh6q2kHu25FkvYBfkddBVCgr/wpgxnukqwlu3xYnmrtNqDJM8louZ38ZGk99miMf6NO1CloleIRc+EA1Vgfq7B/fE8lX1863VlLFViS36G86yUeFagNmPMnnPu7VFOi4RQRnzWa8/fKTGgvLMyGFesv6eWPFgDLEL6wGOpd/0gfxh6vq7sU9yuqQFkLvHTX5j6gf1+9Cfh1yxoJkz36NG3Oo17bIBiSsTqMl5A4Wc/3EKkGaPyjxPRPvxVof4ES0a6pPIA==",
                "SigningCertUrl": "https://sns.eu-west-1.amazonaws.com/SimpleNotificationService-6aad65c2f9911b05cd53efda11f913f9.pem",
                "UnsubscribeUrl": "https://sns.eu-west-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:eu-west-1:849707200000:test-topic:50c099cd-3d0d-4696-903d-cf808b3ce306",
                "MessageAttributes": {
                    "somekey": {
                        "Type": "String",
                        "Value": "dsdasdas"
                    }
                }
            }
        }
    ]
}

## SQS

{
    "Records": [
        {
            "messageId": "72b03944-1af7-492b-aac9-412698960531",
            "receiptHandle": "AQEBicU8TU0NRCELI9WEuVNN/2EU6G1KUJTSVO9BoDPAUvZk4yLsyMQiiKLf0LNmx7ETrdQ1CrwlwdE7JaBVGAcLWPrMCx8wZLxu6x8QtlITVIIAEIwjt+5/rnbmUMY0ajOMok80gO9SceLa+Zr1g3yZnjUMf3xNSGqynvooQkHFRAtSTKf0bkkagkKIHCDt5RghXUeIYruEZTStOm7RTVQwWxaCTQMvbBgNAZFG6j54qg1hCI9Cv9P95FH6Tt8yRDjf9Ad3s7Jykm0yy+IWfvpvHBoSelM03LrUUTX5E6lFpmTByIIE+fmamzeTZOevyNviiiMRbvqvDkDlrVs3M00zjiocX5jz+GVUmE5q0tOzwGYC4yWbcPKDSOc1hiRYUhbo",
            "body": "Howdy?",
            "attributes": {
                "ApproximateReceiveCount": "1",
                "SentTimestamp": "1550567133640",
                "SenderId": "AIDAIEHDD23VAQL6ZVIAC",
                "ApproximateFirstReceiveTimestamp": "1550567133641"
            },
            "messageAttributes": {
                "someattr": {
                    "stringValue": "someval",
                    "stringListValues": [],
                    "binaryListValues": [],
                    "dataType": "String"
                }
            },
            "md5OfBody": "47b68d3722ece13893e9c5fcb3cac906",
            "md5OfMessageAttributes": "1e4b169c724503f3886d943e5bbe88e5",
            "eventSource": "aws:sqs",
            "eventSourceARN": "arn:aws:sqs:eu-west-1:849707200000:test-queue",
            "awsRegion": "eu-west-1"
        }
    ]
}

