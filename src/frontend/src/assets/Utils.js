
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

const renderStatusAtividadeName = (status) =>{
    switch(status){
        case "NOVO":
            return "Nova";
        case "EM_DESENVOLVIMENTO":
            return "Em Desenvolvimento";
        case "FINALIZADO":
            return "Finalizada";
        case "CANCELADO":
            return "Cancelada";
        default:
            return "-";
    }
}

module.exports = {
    renderStatusProjetoName: renderStatusProjetoName,
    renderStatusAtividadeName: renderStatusAtividadeName,
};