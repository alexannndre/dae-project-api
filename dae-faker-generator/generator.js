import { faker } from "@faker-js/faker";

const ALL_TYPES = [
    [
        "vehicle",
        [
            "civil liability",
            "water damage",
            "fire damage",
            "natural disasters",
            "personal possesions",
            "theft and robbery",
        ],
    ],
    [
        "home",
        [
            "civil liability",
            "fire damage",
            "natural disasters",
            "water damage",
            "personal possesions",
        ],
    ],
    ["life", ["death by third party", "death by suicide"]],
    [
        "health",
        [
            "chronic illness",
            "disability",
            "critical ilness",
            "medicine",
            "surgery",
            "dental work",
            "vision",
        ],
    ],
    [
        "travel",
        [
            "cancelation",
            "natural disaster",
            "health",
            "missed transport",
            "delayed departure",
        ],
    ],
    [
        "electronics",
        [
            "water damage",
            "lost possession",
            "theft and robbery",
            "shattered screen",
        ],
    ],
];

const rndType = () => faker.random.arrayElement(ALL_TYPES);

const rndCovers = (covers) => {
    const covLen = faker.datatype.number({ min: 1, max: covers.length });
    let covs = [];
    for (let i = 0; i < covLen; i++) {
        const el = faker.random.arrayElement(covers);
        covs.push(el);
        delete covers[covers.indexOf(el)];
    }
    return covs;
};

const rndVat = () => {
    const first = faker.random.arrayElement([1, 2, 3, 5]);
    const second = faker.datatype.number({ min: 10000000, max: 99999999 });
    return `${first}${second}`;
};

const randomPolicy = () => {
    const code = faker.datatype.uuid();
    const insurer_company = faker.company.name();
    const type = rndType();
    const typeName = type[0];
    const covers = rndCovers(type[1]);
    const customer_vat = rndVat();

    return {
        code,
        customer_vat,
        insurer_company,
        type: typeName,
        covers,
    };
};

console.log(randomPolicy());
console.log(randomPolicy());
console.log(randomPolicy());
console.log(randomPolicy());
console.log(randomPolicy());
console.log(randomPolicy());
