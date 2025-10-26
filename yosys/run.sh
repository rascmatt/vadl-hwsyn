#!/bin/zsh

yosys -s script.ys | tee build/synth.log
