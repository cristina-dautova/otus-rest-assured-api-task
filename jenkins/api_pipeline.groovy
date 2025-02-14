
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

        stage('Running UI tests') {

            status = sh(
                    script: "gradle test",
                    returnStatus: true
            )

            if (status > 0) {
                currentBuild.result = 'UNSTABLE'
            }
        }

        stage('Publish allure report') {
            allure ([
                    includeProperties: true,
                    jdk: '',
                    properties: [],
                    results: [[path: 'build/allure-results']],
                    reportBuildPolicy: 'ALWAYS'
            ])
        }
    }
}