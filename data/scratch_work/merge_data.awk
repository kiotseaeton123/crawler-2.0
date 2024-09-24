BEGIN{
    FS=","
}
FILENAME==ARGV[1]{
    ipv4[$1]=$2
    next
}
FILENAME==ARGV[2]{
    ipv6[$1]=$2
    next
}
FILENAME==ARGV[3] && FNR <= 10{
    if($1 in ipv4 || $1 in ipv6){
        print $1 "," $2 "," $3 "," (ipv4[$1] ? ipv4[$1] : "na") "," (ipv6[$1] ? ipv6[$1] : "na")
    }
}
