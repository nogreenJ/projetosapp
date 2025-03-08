
/**
 * Converte objeto Date para data em formato DD/MM/YYYY
 * @param date objeto Date
 * @returns string de data formato DD/MM/YYYY
 */
const dateObjToBRDate = (date) =>{
    if(!date){
        return "";
    }
    const dateObj = new Date(date);
    if(dateObj == "Invalid Date"){
        return date;
    }
    return dateObj.toLocaleDateString();
}

module.exports = {
    dateObjToBRDate: dateObjToBRDate,
};