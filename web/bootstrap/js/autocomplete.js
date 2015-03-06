

var substringMatcher = function(strs) {
  return function findMatches(q, cb) {
    var matches, substrRegex;
 
    // an array that will be populated with substring matches
    matches = [];
 
    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, 'i');
 
    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function(i, str) {
      if (substrRegex.test(str)) {
        // the typeahead jQuery plugin expects suggestions to a
        // JavaScript object, refer to typeahead docs for more info
        matches.push({ value: str });
      }
    });
 
    cb(matches);
  };
};
 
var states = [
    'ADANA','ADBGR','ADEL','ADESE','ADNAC','AEFES','AFMAS','AFYON','AGYO','AKBNK','AKCNS','AKENR','AKFEN',
    'AKFGY','AKGRT','AKGUV','AKMGY','AKPAZ','AKSA','AKSEN','AKSGY','AKSUE','ALARK','ALBRK','ALCAR','ALCTL',
    'ALGYO','ALKA','ALKIM','ALNTF','ALTIN','ALYAG','ANACM','ANELE','ANELT','ANHYT','ANSA','ANSGR','ARCLK',
    'ARENA','ARFYO','ARMDA','ARSAN','ARTI','ARTOG','ASCEL','ASELS','ASLAN','ASUZU','ASYAB','ATAC','ATAGY',
    'ATEKS','ATLAS','ATPET','ATSYH','AVGYO','AVIVA','AVOD','AVTUR','AYCES','AYEN','AYES','AYGAZ','BAGFS',
    'BAKAB','BAKAN','BALAT','BANVT','BASCM','BAYRD','BERDN','BEYAZ','BFREN','BIMAS','BISAS','BIZIM','BJKAS',
    'BLCYT','BMEKS','BNKTR','BOLUC','BOSSA','BOYNR','BRISA','BRKO','BRKSN','BRMEN','BROVA','BRSAN','BRYAT',
    'BSHEV','BSOKE','BTCIM','BUCIM','BURCE','BURVA','CARFA','CARFB','CBSBO','CCOLA','CELHA','CEMAS','CEMTS',
    'CIMSA','CLEBI','CMBTN','CMENT','COMDO','COSMO','CRDFA','DAGHL','DAGHLR','DAGI','DARDL','DENCM','DENGE',
    'DENIZ','DENTA','DERIM','DESA','DESPC','DEVA','DGATE','DGGYO','DGZTE','DIRIT','DITAS','DJIMT','DJIST',
    'DMSAS','DNZYO','DOAS','DOBUR','DOCO','DOGUB','DOHOL','DURDO','DYHOL','DYOBY','ECBYO','ECILC','ECYAP',
    'ECZYT','EDIP','EGCYH','EGCYO','EGEEN','EGGUB','EGLYO','EGPRO','EGSER','EGYO','EKGYO','EKIZ','EMBYO',
    'EMKEL','EMNIS','ENKAI','EPLAS','ERBOS','EREGL','ERICO','ERSU','ESCOM','ESEMS','ETILR','ETYAT','EUKYO',
    'EUROM','FBIST','FENER','FENIS','FFKRL','FINBN','FLAP','FMIZP','FNSYO','FONFK','FRIGO','FROTO','FVORI',
    'GARAN','GAREN','GARFA','GARON','GDKGS','GDKYO','GEDIK','GEDIZ','GENTS','GEREL','GLBMD','GLDTR','GLRYH',
    'GLYHO','GMSTR','GOLDP','GOLDS','GOLTS','GOODY','GOZDE','GRNYO','GSDDE','GSDHO','GSRAY','GT30','GUBRF',
    'GUSGR','HALKB','HALKS','HATEK','HEKTS','HITIT','HLGYO','HURGZ','HZNDR','ICGYH','IDAS','IDGYO','IEYHO',
    'IHEVA','IHGZT','IHLAS','IHMAD','IHYAY','INDES','INFO','INTEM','IPEKE','ISATR','ISBIR','ISBTR','ISCTR',
    'ISFIN','ISGSY','ISGYO','ISKUR','ISMEN','IST30','ISUVT','ISY30','ISYAT','ISYHO','ITTFH','IZMDC','IZOCM',
    'JANTS','KAPLM','KAREL','KARKM','KARSN','KARTN','KATMR','KCHOL','KENT','KERVN','KERVT','KILER','KIPA',
    'KLBMO','KLGYO','KLMSN','KLNMA','KNFRT','KOMHL','KONYA','KORDS','KOZAA','KOZAL','KPHOL','KRATL','KRDMA',
    'KRDMB','KRDMD','KRONT','KRSAN','KRSTL','KRTEK','KSTUR','KTLME','KUTPO','KUYAS','LATEK','LINK','LKMNH',
    'LOGO','LTHOL','LUKSK','MAALT','MAKTK','MANGO','MARTI','MATAS','MCTAS','MEGAP','MEMSA','MEPET','MERIT',
    'MERKO','METAL','METRO','METUR','MGROS','MIPAZ','MMCAS','MNDRS','MRDIN','MRGYO','MRSHL','MRTGG','MUTLU',
    'MZHLD','NETAS','NIBAS','NTHOL','NTTUR','NUGYO','NUHCM','ODAS','OLMIP','ORGE','ORMA','OSMEN','OSTIM',
    'OTKAR','OYAYO','OYLUM','OZBAL','OZGYO','OZKGY','PAGYO','PARSN','PEGYO','PENGD','PETKM','PETUN','PGSUS',
    'PIMAS','PINSU','PKART','PKENT','PNSUT','POLHO','PRKAB','PRKME','PRTAS','PRZMA','PTOFS','RANLO','RAYSG',
    'RHEAG','ROYAL','RYGYO','RYSAS','SAFGY','SAHOL','SAMAT','SANFM','SANKO','SARKY','SASA','SEKFK','SELEC',
    'SELGD','SERVE','SILVR','SISE','SKBNK','SKPLC','SKTAS','SLVRP','SNGYO','SNPAM','SODA','SODSN','SONME',
    'SRVGY','TACTR','TACYO','TARAF','TATKS','TAVHL','TBORG','TCELL','TCRYO','TEBNK','TEKST','TEKTU','TGSAS',
    'THYAO','TIRE','TKFEN','TKNSA','TKURU','TMSN','TOASO','TRCAS','TRGYO','TRKCM','TRNSK','TSGYO','TSKB',
    'TSPOR','TTKOM','TTRAK','TUDDF','TUKAS','TUPRS','TURSP','UCAK','ULAS','ULKER','UNYEC','USAK','USDTR',
    'UTPYA','UYUM','UZERB','VAKBN','VAKFN','VAKKO','VANGD','VESBE','VESTL','VKBYO','VKGYO','VKING','YAPRK',
    'YATAS','YAZIC','YBTAS','YESIL','YGYO','YKBNK','YKBYO','YKGYO','YKSGR','YONGA','YUNSA','ZOREN'
];
 
$('#searchbox .typeahead').typeahead({
  hint: true,
  highlight: true,
  minLength: 1
},
{
  name: 'states',
  displayKey: 'value',
  source: substringMatcher(states),
  templates: {
    empty: [
      '<div class="empty-message">',
      'Hisse Senedi Bulunamadı',
      '</div>'
    ].join('\n'),
  }
});