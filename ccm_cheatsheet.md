Check that the cluster is correctly set up:
```
ccm node1 ring
```

Check status:
```
ccm status
```

```
ccm start/stop/clear
```

Look at the log file of a given node:
```
ccm node1 showlog
```

Get rid of the cluster:
```
ccm remove
```

Connect with CQLSH:
```
ccm node1 cqlsh

ccm node1 cqlsh -f <file>
```