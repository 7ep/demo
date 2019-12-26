Feature: calculating the portion of a restaurant check that is alcohol-related

    As someone entering an expense at a restaurant
    I want to be able to precisely calculate the amount of the check for the alcohol
    So that I can divide up the check as required when entering expenses

#    Narrative: When entering an expense at a restaurant, government guidelines require
#    that the expense is split up precisely between food and alcohol.  This is not
#    as straightforward as you might immediately think.  For example if the bill is
#    for $50 and you spent $7 on a beer, the expense isn't just $7 for the beer, it
#    also has to consider the alcohol portion of the tip and tax.

    Scenario: Ordering a beer with dinner
        Given a dinner with the following prices in dollars:
            | subtotal | food total | tip | tax  |
            | 33.47    | 24.48      | 7   | 3.99 |
        When I calculate the alcohol-related portion
        Then I get the following results:
            | total food price | total alcohol price | food ratio |
            | 32.52            | 11.94               | 0.7314     |
