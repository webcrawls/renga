const lineLengths = [5, 7, 5]
let latestPosition = 0;

function load() {
    const input = updateTextArea()
    input.addEventListener("keypress", (e) => {
        if (e.code === 13) {
            return
        }
        console.log("--- START ---")
        const sel = window.getSelection()
        const node = sel.focusNode
        const offset = sel.focusOffset
        const pos = getCursorPosition(input, node, offset, {pos: 0, done: false})
        applyWarnings(input, convertToText(input.innerHTML))
        sel.removeAllRanges();
        let range = setCursorPosition(input, document.createRange(), {pos: pos.pos, done: false});
        sel.addRange(range);
        console.log("--- END ---")
    })
}

function updateTextArea() {
    const input = document.querySelector("#haiku-input");
    const newInput = document.createElement("div")
    newInput.contentEditable = true
    newInput.id = "#haiku-input"
    newInput.setAttribute("data-ph", "5, 7, and 5...")
    input.replaceWith(newInput)
    return newInput
}

function updatePostButton() {
    const button = document.querySelector("#haiku-post")
    button.addEventListener("click", (e) => {
        e.preventDefault()
    })
}

function postHaiku() {
    const input = document.querySelector("#haiku-input");
}


function applyWarnings(input, text) {
    const lines = text.split('\n')
    console.log({lines})
    const maxLines = lines.length;
    let nodes = []
    for (const [i, line] of lines.entries()) {
        const maxSyllables = lineLengths[i]
        const syllables = count(line)

        const element = document.createElement("div")
        element.innerText = line;
        if (syllables > maxSyllables) {
            element.classList.add("warning")
        } else if (i > maxLines) {
            element.classList.add("error")
        }
        console.log({element})
        nodes.push(element);
        console.log({nodes})
    }
    input.replaceChildren(...nodes)
}

function count(word) {
    word = word.toLowerCase();
    word = word.replace(/(?:[^laeiouy]|ed|[^laeiouy]e)$/, '');
    word = word.replace(/^y/, '');
    var syl = word.match(/[aeiouy]{1,2}/g);
    if (syl) {
        return syl.length;
    }
}

// thanks to https://gist.github.com/nathansmith/86b5d4b23ed968a92fd4!
const convertToText = (str = '') => {
    // Ensure string.
    let value = String(str);

    // Convert encoding.
    value = value.replace(/&nbsp;/gi, ' ');
    value = value.replace(/&amp;/gi, '&');

    // Replace `<br>`.
    value = value.replace(/<br>/gi, '\n');

    // Replace `<div>` (from Chrome).
    value = value.replace(/<div>/gi, '\n');

    // Replace `<p>` (from IE).
    value = value.replace(/<p>/gi, '\n');

    // Remove extra tags.
    value = value.replace(/<(.*?)>/g, '');

    // No more than 2x newline, per "paragraph".
    value = value.replace(/\n\n+/g, '\n\n');

    console.log({value})

    // Expose string.
    return value;
}

function parse(text) {
    //use (.*?) lazy quantifiers to match content inside
    return text
        .replace(/\*{2}(.*?)\*{2}/gm, '**<strong>$1</strong>**') // bold
        .replace(/(?<!\*)\*(?!\*)(.*?)(?<!\*)\*(?!\*)/gm, '*<em>$1</em>*'); // italic
}

// get the cursor position from .editor start
function getCursorPosition(parent, node, offset, stat) {
    if (stat.done) return stat;

    let currentNode = null;
    if (parent.childNodes.length == 0) {
        stat.pos += parent.textContent.length;
    } else {
        for (var i = 0; i < parent.childNodes.length && !stat.done; i++) {
            currentNode = parent.childNodes[i];
            if (currentNode === node) {
                stat.pos += offset;
                stat.done = true;
                return stat;
            } else
                getCursorPosition(currentNode, node, offset, stat);
        }
    }
    return stat;
}

//find the child node and relative position and set it on range
function setCursorPosition(parent, range, stat) {
    if (stat.done) return range;

    if (parent.childNodes.length == 0) {
        if (parent.textContent.length >= stat.pos) {
            range.setStart(parent, stat.pos);
            stat.done = true;
        } else {
            stat.pos = stat.pos - parent.textContent.length;
        }
    } else {
        for (var i = 0; i < parent.childNodes.length && !stat.done; i++) {
            currentNode = parent.childNodes[i];
            setCursorPosition(currentNode, range, stat);
        }
    }
    return range;
}

load();