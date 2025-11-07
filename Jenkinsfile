pipeline {
    agent any

    parameters {
		
		    choice(
        name: 'ENVIRONMENT',
        choices: ['STAGING', 'DEV', 'PRODUCTION'],
        description: 'Pilih environment untuk pengujian'
    )
        choice(
            name: 'SUITE',
            choices: ['WebSuite.xml', 'APISuite.xml'],
            description: 'Pilih TestNG suite yang ingin dijalankan'
        )

        choice(
            name: 'BROWSER_MODE',
            choices: ['normal', 'headless'],
            description: 'Pilih mode browser untuk WebUI tests'
        )
    }

    tools {
        jdk 'jdk22'      // sesuai konfigurasi JDK di Jenkins
        maven 'maven3'   // sesuai konfigurasi Maven di Jenkins
        allure 'allure'  // Allure plugin di Jenkins
    }

    environment {
        // Set folder Allure default
        ALLURE_RESULTS_WEB = "allure-results-web"
        ALLURE_RESULTS_API = "allure-results-api"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Cloning repository..."
                git branch: 'main', url: 'https://github.com/kennyRamadhan/automation_web_selenium.git'
            }
        }

        stage('Run Test Suite') {
            steps {
                echo " Running selected suite: ${params.SUITE}"

                // Tentukan folder Allure per suite
                script {
                    def allureFolder = params.SUITE.contains("Web") ? env.ALLURE_RESULTS_WEB : env.ALLURE_RESULTS_API
                    // Buat folder jika belum ada
                    sh "mkdir -p ${allureFolder}"
                    // Jalankan Maven test dengan System Property untuk Allure folder
                    sh "mvn clean test -DsuiteFile=${params.SUITE} -Dallure.results.directory=${allureFolder} -DBROWSER_MODE=${params.BROWSER_MODE}"
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    def allureFolder = params.SUITE.contains("Web") ? env.ALLURE_RESULTS_WEB : env.ALLURE_RESULTS_API
                    echo "Generating Allure report for ${allureFolder}..."
                    allure([
                        includeProperties: false,
                        jdk: '',
                        results: [[path: allureFolder]]
                    ])
                }
            }
        }

        stage('Archive Allure Report') {
            steps {
                script {
                    // Rename/copy report per suite supaya artifact terpisah
                    def suiteName = params.SUITE.replace('.xml','')
                    sh "cp -r allure-report allure-report-${suiteName}"
                    archiveArtifacts artifacts: "allure-report-${suiteName}/**", fingerprint: true
                }
            }
        }
    }

    post {
        success {
            echo " Build and tests completed successfully!"
        }
        failure {
            echo " Build failed! Check logs and allure-results for details."
        }
    }
}
