pipeline { 
    agent { 
        label 'ubuntu'
    }
    parameters { 
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'Branch name to build the image from') 
        
    }
    stages { 
        stage('Cloning the Repo'){
            steps {
                git([url: 'https://github.com/syedzaingilani1/apidey-docker.git', branch: '$BRANCH_NAME', credentialsId: 'python-app'])
                
                script{
                    env.tag_name = sh (script: 'echo $BRANCH_NAME  | cut -d"/" -f2-', returnStdout: true).trim()
                    env.image_name = "python-flask:$env.tag_name"
                }
              
            }
        }
        stage('Docker Build'){
            steps{
                sh '''
                    ls -htral
                    short_commit=`git rev-parse --short=7 $BRANCH_NAME`
                    docker-compose -f docker-compose.yaml down
                    docker-compose -f docker-compose.yaml up -d
                    echo "This is to test short commit"
                    echo "Image is created with name  ${image_name}-${short_commit}"
                    ls -lhtra 
                '''
            }
        }
    }
}