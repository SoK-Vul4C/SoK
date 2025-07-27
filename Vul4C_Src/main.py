import argparse
import os.path
import subprocess
import sys
from datetime import datetime

from loguru import logger

import Vul4C_Src.utils as utils
import Vul4C_Src.vul4c_tools as vul4c
from Vul4C_Src.config import VUL4C_DATA, FILE_LOG_LEVEL


# logger
def setup_logger(command: str, display_level: str = "INFO", file_level: str = "DEBUG"):
    log_filename = f"{datetime.now().strftime('%y%m%d_%H%M%S')}_{command}_{file_level}.log"
    logger.remove()
    # STDOUT
    logger.add(sys.stdout,
               colorize=True,
               format="<cyan>{time:YYYY-MM-DD HH:mm:ss}</cyan> | <level>{message}</level>",
               diagnose=False,
               backtrace=False,
               level=display_level.upper())
    # FILE
    logger.add(os.path.join(VUL4C_DATA, "logs", log_filename),
               format="<cyan>{time:YYYY-MM-DD HH:mm:ss}</cyan> | <level>{level}</level> | <level>{message}</level>",
               rotation="00:00",
               level=file_level.upper())


@utils.log_frame("STATUS")
def vul4c_status(_):
    utils.check_status()


@utils.log_frame("CHECKOUT")
def vul4c_checkout(args):
    vul4c.checkout(args.id, args.outdir, args.force)


@utils.log_frame("COMPILE")
def vul4c_compile(args):
    try:
        vul4c.build(args.outdir, clean=args.clean)
    except subprocess.CalledProcessError:
        raise RuntimeError("Compile failed!")


@utils.log_frame("TEST")
def vul4c_test(args):
    try:
        vul4c.test(args.outdir)
    except subprocess.CalledProcessError:
        raise RuntimeError("Testing failed!")


@utils.log_frame("APPLY")
def vul4c_apply(args):
    vul4c.apply(args.outdir, args.patch)


@utils.log_frame("REPRODUCE")
def vul4c_reproduce(args):
    try:
        vul4c.reproduce(args.outdir)
    except subprocess.CalledProcessError:
        raise RuntimeError("Reproduction failed!")


@utils.log_frame("INFO")
def vul4c_info(args):
    vul4c.get_info(args.id)



def main(args=None):
    if args is None:
        args = sys.argv[1:]

    parser = argparse.ArgumentParser(prog="vul4c", description="A Dataset of C/C++ vulnerabilities.")
    parser.add_argument('-l', '--log', type=str, default="INFO",
                        help="Specify displayed log level for this command.")

    sub_parsers = parser.add_subparsers()

    # STATUS
    status_parser = sub_parsers.add_parser("status",
                                           help="Lists vul4c requirements and their availability.")
    status_parser.set_defaults(func=vul4c_status)

    # CHECKOUT
    checkout_parser = sub_parsers.add_parser('checkout',
                                             help="Checkout a vulnerability into the specified directory.")
    checkout_parser.add_argument("-i", "--id", type=str,
                                 help="Vulnerability ID.", required=True)
    checkout_parser.add_argument("-d", "--outdir", type=str,
                                 help="The destination directory.", required=True)
    checkout_parser.add_argument("-f", "--force", action="store_true",
                                 help="Removes the destination directory if it already exists.")
    checkout_parser.set_defaults(func=vul4c_checkout)

    # COMPILE
    compile_parser = sub_parsers.add_parser('compile',
                                            help="Compile the checked out vulnerability.")
    compile_parser.add_argument("-d", "--outdir", type=str,
                                help="The directory to which the vulnerability was checked out.", required=True)
    compile_parser.add_argument("-c", "--clean", action="store_true",
                                help="Clean the project before compiling.")
    compile_parser.set_defaults(func=vul4c_compile)

    # TEST
    test_parser = sub_parsers.add_parser('test',
                                         help="Compile and exploit the checked out vulnerability.")
    test_parser.add_argument("-d", "--outdir", type=str,
                             help="The directory to which the vulnerability was checked out.", required=True)
    test_parser.set_defaults(func=vul4c_test)

    # APPLY
    apply_parser = sub_parsers.add_parser('apply',
                                          help="Apply a patch to the checked out vulnerability.")
    apply_parser.add_argument("-d", "--outdir", type=str,
                              help="The directory to which the vulnerability was checked out.", required=True)
    apply_parser.add_argument("-p", "--patch", type=str,
                              help="Patch path to apply.", required=True)
    apply_parser.set_defaults(func=vul4c_apply)

    # REPRODUCE
    reproduce_parser = sub_parsers.add_parser('reproduce', aliases=["verify"],
                                              help="Exploit the checked out vulnerability.")
    reproduce_parser.add_argument("-d", "--outdir", type=str,
                                  help="The directory to which the vulnerability was checked out.", required=True)
    reproduce_parser.set_defaults(func=vul4c_reproduce)

    # INFO
    info_parser = sub_parsers.add_parser('info',
                                         help="Print information about a vulnerability.")
    info_parser.add_argument("-i", "--id", type=str,
                             help="Vulnerability ID.", required=True)
    info_parser.set_defaults(func=vul4c_info)


    options = parser.parse_args(args)

    if hasattr(options, 'func'):
        setup_logger(options.func.__name__.upper(), options.log, FILE_LOG_LEVEL)
        if not utils.check_config():
            exit(1)
    else:
        parser.print_help()
        exit(1)

    options.func(options)
    exit(0)


if __name__ == "__main__":
    main()