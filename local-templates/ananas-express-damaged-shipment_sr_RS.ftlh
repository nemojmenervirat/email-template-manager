<#if invoiced==false>
  <p>Pošiljka <strong>${suborderId}</strong> je oštećena.</p>
  <p>Za ovu pošiljku nije izdat fiskalni račun refundacija za uslugu dostave.</p>
    <#if paymentMethod=="PBC">
      <p>Sredstva rezervisana potrošaču biće oslobođena.</p>
    </#if>
<#else>
  <p>Pošiljka <strong>${suborderId}</strong> je oštećena od strane kurira i nije preuzeta od strane potrošača.</p>
    <#if paymentMethod=="PBC">
        <#if paymentCompleted>
          <p>Pošto su novčana sredstva za uslugu dostave po ovoj porudžbini prebačena na Vaš račun molimo da izvršite povraćaj novca potrošaču u skladu sa fiskalnim računom refundacija za uslugu dostave koji je u prilogu. Prosledićemo Vam tekući račun kupca.</p>
        <#else>
          <p>Pokrenut je proces automatskog povraćaja novca potrošaču u skladu sa fiskalnim računom refundacija za uslugu dostave, koji je u prilogu.</p>
        </#if>
    </#if>
    <#if paymentMethod=="COD">
      <p>U prilogu dostavljamo fiskalni račun refundacija za uslugu dostave.</p>
    </#if>
</#if>
<p>Ukoliko imate pitanja pozovite nas na <strong>${customerSupportPhone}</strong> ili pišite na <a href="mailto:${customerSupportEmail}">${customerSupportEmail}</a>.</p>