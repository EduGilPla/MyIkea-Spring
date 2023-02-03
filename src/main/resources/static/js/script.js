const DOM = {
  selectProvincia: document.getElementById("selectProvincia"),
  selectMunicipio: document.getElementById("selectMunicipio"),
  inputFile: document.getElementById("picture"),
  hiddenPictureInput: document.getElementById("pictureValue")
}

let Provincias = [];

(async function (){
  DOM.selectProvincia.addEventListener("change",loadMunicipios);
  DOM.inputFile.addEventListener("change",addPictureValue);
  Provincias = await fetch("/data/provincias.json").then(response => response.json());
  //loadMunicipios();
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
function addPictureValue(){
  let valueArray = DOM.inputFile.value.split("\\");
  let fileName = valueArray[valueArray.length-1];
  DOM.hiddenPictureInput.value = fileName;
}

