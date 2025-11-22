pipeline {
    agent any

    tools {
        maven 'maven-3'
    }

    environment {
        SONAR_PROJECT_KEY = 'springboot-local'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('local-sonar') {
                    sh '''
                        mvn -B sonar:sonar \
                          -Dsonar.projectKey='''' + "${SONAR_PROJECT_KEY}" + '''' \
                          -Dsonar.host.url=$SONAR_HOST_URL \
                          -Dsonar.login=$SONAR_AUTH_TOKEN
                    '''
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B package -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
