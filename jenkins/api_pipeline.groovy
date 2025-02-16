
timeout(time: 30, unit: 'MINUTES') {

    node('gradle') {

        stage('Checkout') {
            checkout scm
        }

        stage('Checkout utils') {
            dir('jenkins') {
                git branch: 'main', url: 'https://github.com/cristina-dautova/otus-test-runner.git', credentialId: 'jenkins'
            }
        }

        utils = load './jenkins/utils'
        utils.prepare_yaml_config


        stage('Run Tests') {
            status = sh(
                    script: "gradle test",
                    returnStatus: true
            )

            if (status > 0) {
                currentBuild.result = 'UNSTABLE'
            }
        }

        stage('Verify Allure Results') {
            script {
                def allureResultsDir = 'build/allure-results'
                if (fileExists(allureResultsDir)) {
                    echo 'Allure results exist'
                    sh "ls -la ${allureResultsDir}"
                } else {
                    echo 'Allure results do not exist or directory is empty'
                }
            }
        }

        stage('Publish allure report') {
            allure ([
                    includeProperties: true,
                    jdk: '',
                    properties: [],
                    results: [[path: 'build/allure-results']],
                    reportBuildPolicy: 'ALWAYS',
                    report: 'build/allure-report'
            ])
        }
    }
}