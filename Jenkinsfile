pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }
    environment {
        SCANNER_HOME = tool 'sonar-scanner'
        SONAR_TOKEN = credentials('sonar-token')
        IMAGE_NAME = 'subhadipj/greengrab'
        KUBECONFIG = "/var/lib/jenkins/.kube/config"
    }

    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'dev', credentialsId: 'git-cred', url: 'https://github.com/BITSSAP2025AugAPIBP3Sections/APIBP-20234YC-Team-01.git'
            }
        }
        stage('Build Peoject') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('Run Test Cases') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarCloud') {
                    sh '''
                    mvn sonar:sonar \
                    -Dsonar.projectKey=GreenGrab \
                    -Dsonar.projectName=GreenGrab \
                    -Dsonar.organization=subhadipj \
                    -Dsonar.host.url=https://sonarcloud.io \
                    -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."
                    sh 'docker version'
                    withDockerRegistry(credentialsId: 'docker-cred') {
                        sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo "Pushing image to DockerHub..."
                    withDockerRegistry(credentialsId: 'docker-cred') {
                        sh "docker push ${IMAGE_NAME}:${BUILD_NUMBER}"
                    }
                }
            }
        }

        stage('Update Kubernetes Manifests') {
            steps {
                script {
                    echo "Substituting Docker image tag in k8s YAML..."
                    sh """
                        sed -i 's@IMAGE_NAME@${IMAGE_NAME}:${BUILD_NUMBER}@g' k8s/greengrub-deployment.yaml
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Apply Kubernetes manifests
                    echo "Applying Kubernetes Deployment..."
                    sh "cat k8s/greengrub-deployment.yaml"
                    sh "kubectl --kubeconfig=${KUBECONFIG} apply -f k8s/greengrub-deployment.yaml --validate=false"

                    echo "Applying Kubernetes Service..."
                    sh "kubectl --kubeconfig=${KUBECONFIG} apply -f k8s/greengrub-service.yaml --validate=false"

                    echo "Waiting for deployment to finish rolling out..."
                    sh "kubectl --kubeconfig=${KUBECONFIG} rollout status deployment/greengrub-deployment"
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    sh "kubectl get pods"
                    sh "kubectl get svc"
                }
            }
        }
    }
    post {
        success {
            echo " ✅ Deployment succeeded"
        }
        failure {
            echo " ❌ Pipeline failed"
        }
    }

}