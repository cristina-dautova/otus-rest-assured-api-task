
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

        environment {
            IMAGE_NAME = "java-tests"
            CONTAINER_NAME = "java-tests-container"
        }

        stage('Build Docker Image') {
                script {
                    docker.build("${IMAGE_NAME}")
                }
        }

        stage('Run Tests in Docker') {

                    status = sh(
                            script: """
                            docker run --rm --name ${CONTAINER_NAME} ${IMAGE_NAME}
                        """,
                            returnStatus: true
                    )

                    if (status > 0) {
                        currentBuild.result = 'UNSTABLE'
                    }

        }

        stage('Generate Allure Report') {
                script {
                    sh """
                        docker cp ${CONTAINER_NAME}:/app/build/allure-results ./build/allure-results || true
                        allure generate allure-results --clean -o allure-report
                    """
                }
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
                script {
                    sh "docker rm -f ${CONTAINER_NAME} || true"
                }
            }
        }
    }
}