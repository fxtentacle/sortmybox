# Makefile for the anza project
all: deps

run: all
	play run

deps: .lastdepsrun

deploy: deps
	play gae:deploy
	git push origin master:gae

.lastdepsrun: conf/dependencies.yml
	play deps --sync
	play ec
	date > .lastdepsrun

clean:
	play clean

superclean:
	# RUN THIS AT YOUR OWN RISK, THIS WILL DELETE EVERY UNTRACKED FILE 
	git clean -dxf

