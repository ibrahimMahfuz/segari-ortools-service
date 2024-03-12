#!/usr/bin/env bash

set -ex #e: fail fast. x: verbose output

ECR_HOST=916018081767.dkr.ecr.ap-southeast-1.amazonaws.com
ECR_REPO=segari-ortools

git checkout $COMMIT_HASH

#Connect to EXR
aws sts get-caller-identity
aws ecr get-login-password --region ap-southeast-1 | docker login --username AWS --password-stdin $ECR_HOST

#Build docker image
docker build -t $ECR_HOST/$ECR_REPO:$GIT_COMMIT .
docker push $ECR_HOST/$ECR_REPO:$GIT_COMMIT

# Create new revision for deployment
CLUSTER_NAME="$CLUSTER_NAME"
 # CLUSTER_NAME exported from jenkins job
TASK_NAME="segari-ortools-$CLUSTER_NAME"
SERVICE_NAME="segari-ortools-$CLUSTER_NAME"
REGION="ap-southeast-1"

NEW_IMAGE="$ECR_HOST/$ECR_REPO:$GIT_COMMIT"
aws ecs describe-task-definition --task-definition "$TASK_NAME" --region "$REGION" > task-definition.json
cat task-definition.json | jq --arg IMAGE "$NEW_IMAGE" '.taskDefinition | .containerDefinitions[0].image = $IMAGE | del(.taskDefinitionArn) | del(.revision) | del(.status) | del(.requiresAttributes) | del(.compatibilities) | del(.registeredAt) | del(.registeredBy)' > new-task-definition.json
aws ecs register-task-definition --region "$REGION" --cli-input-json file://new-task-definition.json >/dev/null
aws ecs update-service --cluster $CLUSTER_NAME --service $SERVICE_NAME --task-definition $TASK_NAME --force-new-deployment >/dev/null
 
#cleanup
rm -f task-definition.json new-task-definition.json

