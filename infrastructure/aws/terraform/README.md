# Install the AWS CLI
## Linux:
1.You need first install Python(recommend Python3), then proceed to installing pip(recommend pip3) and the AWS CLI.
<pre><code>sudo apt-get install python3</code></pre>
2.Install pip
<pre><code>sudo apt-get install pip3</code></pre>
3.Install the AWS CLI with pip
<pre><code>pip3 install awscli --upgrade --user</code></pre>
<pre><code>export PATH=~/.local/bin:$PATH</code></pre>

# Configuring the AWS CLI
## Linux:
1.Type this command
<pre><code>aws configure --profile "your profile name"</code></pre>
2.When you type this command, the AWS CLI prompts you for four pieces of information (access key, secret access key, AWS Region, and output format). Like following:
<pre><code>AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE</code></pre>
<pre><code>AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY</code></pre>
<pre><code>Default region name [None]: us-west-2</code></pre>
<pre><code>Default output format [None]: json</code></pre>

# Install the Terraform
## Linux:
1.Make sure you have installed wget
<pre><code>sudo apt-get install wget </code></pre>
2.Use wget to istanll the Terraform
<pre><code>wget https://releases.hashicorp.com/terraform/0.12.10/terraform_0.12.10_linux_amd64.zip</code></pre>
3.Unzipe the Terraform
<pre><code>sudo apt-get install unzip</code></pre>
<pre><code>unzip terraform_0.12.10_linux_amd64.zip</code></pre>
4.Move the Terraform binary file to "/usr/local/bin/"</code></pre>
<pre><code>sudo mv terraform /usr/local/bin/</code></pre>

# Run the Terraform
1.In terminal, move to your work directory
2.Initial Terraform work environment
<pre><code>terraform init</code></pre>
3.Check the Terrafrom's output(optional)
<pre><code>terraform plan</code></pre>
4.Apply Changes
<pre><code>terraform apply</code></pre>
5.Enter the require information like following
(In all_subnet_cidr_block field, different block should be split by ',')
<pre><code>var.all_subnet_cidr_block
  Enter a value: 10.0.1.0/24,10.0.2.0/24,10.0.3.0/24

var.aws_region
  Enter a value: us-east-1

var.profile_name
  Enter a value: dev

var.vpc_cidr_block
  Enter a value: 10.0.0.0/16

var.vpc_name
  Enter a value: qq</code></pre>