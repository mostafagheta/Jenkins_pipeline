pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk11'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/mostafagheta/Jenkins_pipeline.git'
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
                sh '''
                    dp \
                      --project "Jenkins-pipeline" \
                      --scan . \
                      --format "XML" \
                      --format "HTML" \
                      --out dependency-check-report
                '''
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
    }

    post {
        success {
            archiveArtifacts 'target/*.jar'
        }
    }
}
