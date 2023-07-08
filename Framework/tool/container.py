import os
import docker
import random

from logger import logger

class DockerContainer(object):
    
    image_name = ""
    repository_name=""
    tag_name = ""
    image=None
    
    container = None
    container_name = ""

    localhost_dir = ""
    container_dir = ""

    container_status=None
    
    gpu=False

    client = None

    def __init__(self, repository_name, tag_name, container_name, localhost_dir, container_dir, gpu=False):
        self.client = docker.from_env()
        self.repository_name=repository_name
        self.tag_name = tag_name
        self.container_name = container_name
        self.localhost_dir=localhost_dir
        self.container_dir=container_dir
        self.gpu=gpu

        self.image_name=f"{self.repository_name}:{self.tag_name}"
        if self.image_exists():
            self.image=self.get_image()
        else:
            self.pull_image()

        if not self.find_container():
            self.build_container()
        
        self.get_container_status()
        print(self.container_status)

        if self.container_status=="exited":
            self.start_container()
        elif self.container_status=="running" or self.container_status=="created":
            pass
        else:
            raise RuntimeError(f"the container {self.container.id} status is restarting or pause")

    def image_exists(self):
        try:
            image_list = self.client.images.list()
        except IOError as ex:
            logger.error(f"{ex}")
            raise ex
        for image in image_list:
            tag_list = image.tags  
            if not tag_list:
                continue
            if self.repository_name != tag_list[0].split(":")[0]:
                continue
            for tag in tag_list:
                _, tag_id = tag.split(":")
                if self.tag_name == tag_id:
                    return True
        return False


    def get_image(self):
        try:
            image_list = self.client.images.list()
        except IOError as ex:
            logger.error(f"{ex}")
            raise ex
        for image in image_list:
            tag_list = image.tags 
            if not tag_list:
                continue
            if self.repository_name != tag_list[0].split(":")[0]:
                continue
            for tag in tag_list:
                _, tag_id = tag.split(":")
                if self.tag_name == tag_id:
                    self.image = image


    def pull_image(self):
        logger.info(f"pulling docker image {self.image_name}")
        image = None
        try:
            self.image = self.client.images.pull(repository=self.repository_name, tag=self.tag_name)
            logger.info(f"the docker image {self.image_name} id: {self.image.id}")
        except docker.errors.APIError as ex: 
            logger.error(f"{ex}")
            logger.error("unable to pull image: docker daemon error")
            raise ex
        except IOError as ex:
            logger.error(f"{ex}")
            raise ex
        except Exception as ex:
            logger.error(f"{ex}")
            logger.error("unable to pull image: unhandled exception")
            raise ex


    def find_container(self):

        try:
            self.container = self.client.containers.get(self.container_name)
        except docker.errors.NotFound as ex: 
            logger.warning("Unable to find container")
            return False
        except docker.errors.APIError as ex:
            logger.error(f"{ex}")
            logger.error("unable to find container: docker daemon error")
            raise ex
        except IOError as ex:
            logger.error(f"{ex}")
            raise ex
        except Exception as ex:
            logger.error(f"{ex}")
            logger.error("unable to find container: unhandled exception")
            raise ex
        return True

    def get_container_status(self):
        self.container_status = self.container.status

    def build_container(self):
        logger.info(f"building docker container based on image {self.image_name} with name {self.container_name}")
        try:
            container_run_args = {
                "detach": True,
                "name": self.container_name,
                "volumes": ['{}:{}'.format(self.localhost_dir, self.container_dir)],
                "privileged": True,
                "tty": True,
                "command": "bash",
            }
            if self.gpu:
                container_run_args["device_requests"]=[docker.types.DeviceRequest(count=-1, capabilities=[['gpu']])]
        
            logger.debug(f"container {self.container_name} is build with the following args {container_run_args}")
            self.container = self.client.containers.run(self.image_name, **container_run_args)

        except docker.errors.ContainerError as ex: 
            logger.error(ex)
            logger.error("unable to build container: container exited with a non-zero exit code")
            raise ex
        except docker.errors.ImageNotFound as ex:
            logger.error(ex)
            logger.error("unable to build container: image not found")
            raise ex
        except docker.errors.APIError as ex: 
            logger.error(ex)
            logger.error("unable to build container: docker daemon error")
            raise ex
        except IOError as ex:
            logger.error(ex)
            raise ex
        except Exception as ex:
            logger.error(ex)
            logger.error("unable to build container: unhandled exception")
            raise ex


    def exec_command(self, command: str, workdir="/", env = dict(),user="root"):
        try:
            docker_cmd = "[{}] {}".format(workdir, command)
            logger.info(f"[DOCKER_CMD] {docker_cmd}")
            result = self.container.exec_run( 
                command,
                privileged=True,
                workdir=workdir,
                tty=True,
                environment=env,
                user=user
            )
            exit_code=result.exit_code
            output=result.output.decode('utf-8')
            if output is not None:
                for line in output.split("\n"):
                    if line != "":
                        logger.debug(line)
        except docker.errors.APIError as ex:
            logger.error(ex)
            logger.error(f"the container {self.container.id} server returns an error.")
            raise ex
        
        return exit_code, output


    def start_container(self):
        logger.info(f"starting docker container {self.container.id}")
        try:
            self.container.start() 
            self.get_container_status()
        except docker.errors.APIError as ex:
            logger.error(ex)
            logger.error("unable to start container: docker daemon error")
            raise ex
        except Exception as ex:
            logger.error(ex)
            logger.error("unable to start container: unhandled exception")
            raise ex

    def stop_container(self):
        logger.info(f"stopping docker container {self.container.id}")
        try:
            self.container.stop(timeout=20) 
            self.get_container_status()
        except docker.errors.APIError as ex: 
            logger.warning(ex)
            logger.warning("unable to stop container: docker daemon error")

        except Exception as ex:
            logger.warning(ex)
            logger.warning("unable to stop container: unhandled exception")


    def file_exists(self, file_path):
        exist_command = f"test -f {file_path}"
        return self.exec_command(exist_command)[0] == 0

    def dir_exists(self, dir_path):
        exist_command = f"test -d {dir_path}"
        return self.exec_command(exist_command)[0] == 0

    def find_file(self, dir_path, maxdepth, name):
        find_command = f"find {dir_path} -maxdepth {maxdepth} -name {name}"
        return self.exec_command(find_command)[1].split("\n")
    
    def cp_file(self, from_path, to_path):
        cp_command = f"cp -r {from_path} {to_path}"
        return self.exec_command(f"bash -c \"{cp_command}\"")[0] == 0

    def add_permissions(self, file_path):
        permission_command = f"chmod -R a+x  {file_path}"
        return self.exec_command(f"bash -c \"{permission_command}\"") != 0
