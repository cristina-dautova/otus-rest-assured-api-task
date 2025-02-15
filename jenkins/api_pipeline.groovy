
timeout(time: 30, unit: 'MINUTES') {

    node('gradle') {

        stage('Checkout') {
            checkout scm
        }

//        stage('Checkout utils') {
//            dir('utils') {
//                git branch: 'main', url: 'https://github.com/cristina-dautova/otus-test-runner.git', credentialId: 'jenkins'
//            }
//        }
//
//        utils = load './tools/utils'
//        utils.prepare_yaml_config


        stage('Run Tests') {
            sh 'gradle test'
        }


        stage('Verify Allure Results') {

            sh 'ls -la build/allure-results || true'

            if (fileExists("build/allure-results")) {
                echo 'allure results exist'
            }

        }

        stage('Publish allure report') {
            allure ([
                    includeProperties: true,
                    jdk: '',
                    properties: [],
                    results: [[path: './build/allure-results']],
                    reportBuildPolicy: 'ALWAYS'
            ])
        }
    }
}