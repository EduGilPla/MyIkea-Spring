const DOM = {
  selectProvincia: document.getElementById("selectProvincia"),
  selectMunicipio: document.getElementById("selectMunicipio")
}

let Provincias = [];

(async function (){
  DOM.selectProvincia.addEventListener("change",loadMunicipios);
  Provincias = await fetch("/provincias.json").then(response => response.json());
})();
function loadMunicipios(){
  while(DOM.selectMunicipio.firstChild){
    DOM.selectMunicipio.removeChild(DOM.selectMunicipio.lastChild);
  }
  let province = Provincias.find(prov => prov.id_provincia == DOM.selectProvincia.value);

  province.municipios.forEach(municipio => {
    let option = document.createElement("option");
    option.value=municipio.id_municipio;
    option.textContent = municipio.nombre;
    DOM.selectMunicipio.insertAdjacentElement("beforeend",option);
  });

}

