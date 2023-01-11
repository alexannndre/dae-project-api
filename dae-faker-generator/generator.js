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

const rndArrayElement = (arr) => {
    return arr[faker.datatype.number({ max: arr.length - 1 })];
};

const rndType = () => rndArrayElement(ALL_TYPES);

const rndCovers = (_covers) => {
    let covers = [..._covers];
    const covLen = faker.datatype.number({ min: 1, max: covers.length });
    let covs = [];
    for (let i = 0; i < covLen; i++) {
        const el = rndArrayElement(covers);
        covs.push(el);
        covers.splice(covers.indexOf(el), 1);
    }
    return covs;
};

const rndVat = () => {
    const first = rndArrayElement([1, 2, 3, 5]);
    const second = faker.datatype.number({ min: 10000000, max: 99999999 });
    return `${first}${second}`;
};

const randomPolicy = () => {
    const code = faker.random.numeric(5);
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

const allPolicies = [];

for (let a = 0; a < 20; a++) allPolicies.push(randomPolicy());

console.log(allPolicies);
