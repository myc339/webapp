#Install the AWS CLI
##Linux:
1.You need first install Python(recommend Python3), then proceed to installing pip(recommend pip3) and the AWS CLI.
<pre><code>sudo apt-get install python3</code></pre>
2.Install pip
<pre><code>curl -O https://bootstrap.pypa.io/get-pip.py</code></pre>
<pre><code>python3 get-pip.py --user</code></pre>
<pre><code>export PATH=~/.local/bin:$PATH</code></pre>
3.Install the AWS CLI with pip
<pre><code>pip3 install awscli --upgrade --user</code></pre>

#Configuring the AWS CLI
##Linux
1.Type this command
<pre><code>aws configure --profile "your profile name"<pre><code>
2.When you type this command, the AWS CLI prompts you for four pieces of information (access key, secret access key, AWS Region, and output format). Like following:
<pre><code>AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE</code></pre>
<pre><code>AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY</code></pre>
<pre><code>Default region name [None]: us-west-2</code></pre>
<pre><code>Default output format [None]: json</code></pre>