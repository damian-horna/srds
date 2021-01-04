ccm remove
#sudo ifconfig lo0 alias 127.0.0.2 up
#sudo ifconfig lo0 alias 127.0.0.3 up
#sudo ifconfig lo0 alias 127.0.0.4 up
ccm create test -v 3.11.8 -n 3 -s
ccm node1 cqlsh -f schema.cql