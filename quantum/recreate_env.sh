#!/usr/bin/env bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

rm -rf venv
pip install virtualenv
virtualenv $DIR/venv
source $DIR/venv/bin/activate
pip install -r $DIR/requirements.txt
# https://learn.qiskit.org/course/ch-prerequisites/environment-setup-guide-to-work-with-qiskit-textbook
pip install git+https://github.com/qiskit-community/qiskit-textbook.git#subdirectory=qiskit-textbook-src