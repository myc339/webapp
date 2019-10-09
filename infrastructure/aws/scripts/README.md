# Install the AWS CLI
## Linux:
1.You need first install Python(recommend Python3), then proceed to installing pip(recommend pip3) and the AWS CLI.
<pre><code>sudo apt-get install python3</code></pre>
2.Install pip
<pre><code>sudo apt-get install pip3</code></pre>
3.Install the AWS CLI with pip
<pre><code>pip3 install awscli --upgrade --user</code></pre>
<pre><code>export PATH=~/.local/bin:$PATH</code></pre>>

# Configuring the AWS CLI
## Linux
1.Type this command
<code><code>aws configure --profile "your profile name"</code></code>
2.When you type this command, the AWS CLI prompts you for four pieces of information (access key, secret access key, AWS Region, and output format). Like following:
<pre><code>AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE</code></pre>
<pre><code>AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY</code></pre>
<pre><code>Default region name [None]: us-west-2</code></pre>
<pre><code>Default output format [None]: json</code></pre>

# Install jq dependency 
## Linux:
1.Open your sources file in a text editor:
<pre><code>sudo vim /etc/apt/sources.list</code></pre>
2.Add the following line to the end of that file (note deb is not a command, more info):
<pre><code>deb http://us.archive.ubuntu.com/ubuntu vivid main universe</code></pre>
3.Then re-index apt-get so that it can find jq:
<pre><code>sudo apt-get update </code></pre>
4.Then do the normal install and you should be the proud new user of jq!
<pre><code>sudo apt-get install jq </code></pre>

# Run 
Run command below in scripts directory:
<li>Switch User</li>
<pre><code>aws s3 ls --profile [profile name]</code></pre>
<li>set aws region </li>
<pre><code>aws configure set region [region] --profile [profile name]</code></pre>
<li>Create VPC Resource</li>
<pre><code>./csye6225-aws-networking-setup.sh</code></pre>
<li>Delete VPC Resource</li>
<pre><code>./csye6225-aws-networking-teardown.sh</code></pre>