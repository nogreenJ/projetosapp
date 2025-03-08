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