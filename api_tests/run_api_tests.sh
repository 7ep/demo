#!/bin/sh

cd ~/demo_tests/api_tests
pipenv run pytest --junitxml ../build/test-results/api_tests/TEST-api_test_results.xml