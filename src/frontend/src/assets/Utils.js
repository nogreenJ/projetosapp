
const renderStatusProjetoName = (status) =>{
    switch(status){
        case "EM_DESENVOLVIMENTO":
            return "Em Desenvolvimento";
        case "FINALIZADO":
            return "Finalizado";
        case "CANCELADO":
            return "Cancelado";
        default:
            return "-";
    }
}

module.exports = {
    renderStatusProjetoName: renderStatusProjetoName,
};