#install jq dependency 
##linux:
1.Open your sources file in a text editor:
<pre><code>sudo vim /etc/apt/sources.list</code></pre>
2.Add the following line to the end of that file (note deb is not a command, more info):
<pre><code>deb http://us.archive.ubuntu.com/ubuntu vivid main universe</code></pre>
3.Then re-index apt-get so that it can find jq:
<pre><code>sudo apt-get update </code></pre>
4.Then do the normal install and you should be the proud new user of jq!
<pre><code>sudo apt-get install jq </code></pre>

#run 
Run command below in scripts directory:
<li>Switch User</li>
<pre><code>aws s3 ls --profile [profile name]</code></pre>
<li>set aws region </li>
<pre><code>aws configure set region [region] --profile [profile name]</code></pre>
<li>Create VPC Resource</li>
<pre><code>./csye6225-aws-networking-setup.sh</code></pre>
<li>Delete VPC Resource</li>
<pre><code>./csye6225-aws-networking-teardown.sh</code></pre>

