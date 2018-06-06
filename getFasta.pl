use LWP::Simple;

#append [accn] field to each accession
for ($i=0; $i < @ARGV; $i++) {
   $ARGV[$i] .= "[accn]";
}

#join the accessions with OR
$query = join('+OR+',@ARGV);

#assemble the esearch URL
$base = 'https://eutils.ncbi.nlm.nih.gov/entrez/eutils/';
$url = $base . "esearch.fcgi?db=nuccore&term=$query&usehistory=y";

#post the esearch URL
$output = get($url);

#parse WebEnv and QueryKey
$web = $1 if ($output =~ /<WebEnv>(\S+)<\/WebEnv>/);
$key = $1 if ($output =~ /<QueryKey>(\d+)<\/QueryKey>/);

#assemble the efetch URL
$url = $base . "efetch.fcgi?db=nuccore&query_key=$key&WebEnv=$web";
$url .= "&rettype=fasta&retmode=text";

#post the efetch URL
$fasta = get($url);

my $filename = 'dna.txt';
 
open(FH, '>', $filename) or die $!;
 
print FH $fasta;
 
close(FH);