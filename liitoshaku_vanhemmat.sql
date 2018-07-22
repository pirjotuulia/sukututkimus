SELECT lapsi.etunimi, lapsi.sukunimi, lapsi.syntymaaika,
  aiti.etunimi as aidin_etunimi, aiti.sukunimi as aidin_sukunimi, aiti.syntymaaika as aidin_syntymaaika,
  isa.etunimi as isan_etunimi, isa.sukunimi as isan_sukunimi, isa.syntymaaika as isan_syntymaaika FROM henkilo AS lapsi
  JOIN henkilo AS aiti ON (aiti.id = lapsi.aiti)
  JOIN henkilo AS isa ON (isa.id = lapsi.isa);