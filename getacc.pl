use LWP::Simple;
#$gi_list = '24475906,224465210,50978625,9507198';

#assemble the URL
$base = 'https://eutils.ncbi.nlm.nih.gov/entrez/eutils/';
$url = $base . "efetch.fcgi?db=nucleotide&id=$ARGV[0]&rettype=acc";

#post the URL
$output = get($url);
my $filename = 'acc_num.txt';
 
open(FH, '>', $filename) or die $!;
 
print FH $output; 
 
close(FH);