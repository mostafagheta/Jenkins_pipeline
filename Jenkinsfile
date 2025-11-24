pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk11'
    }
    environment {
        DOCKER_IMAGE = 'mostafagheta/shopping-app:latest'
    }
    stages {

        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/mostafagheta/Jenkins_pipeline.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                sh 'mkdir -p dependency-check-report'
                dependencyCheck(
                    odcInstallation: 'dp',
                    additionalArguments: '--project Jenkins-pipeline --scan . --format XML --format HTML --out dependency-check-report'
                )
            }
            post {
                always {
                    archiveArtifacts artifacts: 'dependency-check-report/*', fingerprint: true
                }
            }
        }

        stage('Publish Reports') {
            steps {
                // Publish Surefire Test HTML report (if generated)
                publishHTML(target: [
                    reportName: 'Unit Test Report',
                    reportDir: 'target/surefire-reports',
                    reportFiles: 'index.html',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])

                // Publish OWASP Dependency-Check HTML report
                publishHTML(target: [
                    reportName: 'OWASP Dependency Check',
                    reportDir: 'dependency-check-report',
                    reportFiles: 'dependency-check-report.html',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
            }
        }
        stage('SonarQube Analysis') {
            environment {
                SONARQUBE_TOKEN = credentials('sonarqube')
            }
            steps {
                script {
                    withSonarQubeEnv('jenkins') {   // Jenkins Sonar server name
                        sh """
                            ${tool 'sonarqube'}/bin/sonar-scanner \
                            -Dsonar.projectKey=Shopping-App \
                            -Dsonar.projectName=Shopping-App \
                            -Dsonar.sources=src \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.login=${SONARQUBE_TOKEN}
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 2, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        
         stage('Build Docker Image') {
            steps {
                echo "Building Docker image ${DOCKER_IMAGE}"
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Scan Docker Image with Trivy') {
            steps {
                echo "Scanning Docker image ${DOCKER_IMAGE} with Trivy"
                sh "trivy image --format table --exit-code 1 --severity HIGH,CRITICAL ${DOCKER_IMAGE}"
                sh "trivy image --format json -o trivy-report.json ${DOCKER_IMAGE}"
                sh "trivy image --format template -o trivy-report.html --template '@contrib/html.tpl' ${DOCKER_IMAGE}"
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-report.*', fingerprint: true
                }
            }
        }
    }

    post {
        success {
            archiveArtifacts 'target/*.jar'
        }
    }
}
