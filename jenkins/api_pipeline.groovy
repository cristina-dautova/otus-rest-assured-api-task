
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



        stage('Validate Dockerfile') {

            script {

                if (!fileExists('Dockerfile')) {
                    error "Dockerfile not found in the repository root"
                }

                def dockerfile = readFile('Dockerfile')

                if (!dockerfile.toLowerCase().trim().contains('from ')) {
                    error "Dockerfile validation failed: No source image provided with FROM instruction"
                }

                echo "Dockerfile validation passed"
            }
        }

        stage('Run Tests') {
            sh 'gradle test'
        }


        stage('Verify Allure Results') {

            sh 'ls -la build/allure-results || true'

        }
//        stage('Running UI tests') {
//
//            status = sh(
//                    script: "gradle test",
//                    returnStatus: true
//            )
//
//            if (status > 0) {
//                currentBuild.result = 'UNSTABLE'
//            }
//        }

        stage('Publish allure report') {
            allure ([
                    includeProperties: true,
                    jdk: '',
                    properties: [],
                    results: [[path: 'build/allure-results']],
                    reportBuildPolicy: 'ALWAYS'
            ])
        }

        post {
            always {
                cleanWs()
            }
            failure {
                echo 'Pipeline failed during Dockerfile validation'
            }
        }
    }
}